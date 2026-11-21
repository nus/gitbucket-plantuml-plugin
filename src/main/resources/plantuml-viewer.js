(() => {
  "use strict";

  const minScale = 0.1;
  const maxScale = 5;
  // The design does not specify a zoom step; use the same factor for buttons and wheel.
  const zoomStep = 1.2;
  const clamp = (value, lower, upper) => Math.min(upper, Math.max(lower, value));

  class SvgViewerViewport {
    constructor(element, image) {
      this.element = element;
      this.image = image;
      this.height = Math.min(600, image.naturalHeight);
      this.width = image.naturalWidth * (this.height / image.naturalHeight);
      this.scale = 1;
      this.x = 0;
      this.y = 0;
      element.style.setProperty("--plantuml-viewer-height", `${this.height}px`);
      image.style.width = `${this.width}px`;
      image.style.height = `${this.height}px`;
      this.keepIntersectionVisible();
      new ResizeObserver(() => this.keepIntersectionVisible()).observe(element);
    }

    setScale(requestedScale, anchor) {
      const scale = clamp(requestedScale, minScale, maxScale);
      const x = anchor ? anchor.x : this.element.clientWidth / 2;
      const y = anchor ? anchor.y : this.element.clientHeight / 2;
      const ratio = scale / this.scale;
      this.x = x - (x - this.x) * ratio;
      this.y = y - (y - this.y) * ratio;
      this.scale = scale;
      this.keepIntersectionVisible();
    }

    panBy(dx, dy) {
      this.x += dx;
      this.y += dy;
      this.keepIntersectionVisible();
    }

    fitToView() {
      const {clientWidth, clientHeight} = this.element;
      const scale = Math.min(clientWidth / this.width, clientHeight / this.height, maxScale);
      // Below the zoom floor neither a full fit nor a change of the floor is specified.
      if (scale < minScale) return false;

      this.scale = scale;
      this.x = (clientWidth - this.width * scale) / 2;
      this.y = (clientHeight - this.height * scale) / 2;
      this.keepIntersectionVisible();
      return true;
    }

    keepIntersectionVisible() {
      const {clientWidth, clientHeight} = this.element;
      if (!clientWidth || !clientHeight) return;

      const width = this.width * this.scale;
      const height = this.height * this.scale;
      const visibleX = Math.min(1, width);
      const visibleY = Math.min(1, height);
      this.x = clamp(this.x, visibleX - width, clientWidth - visibleX);
      this.y = clamp(this.y, visibleY - height, clientHeight - visibleY);
      this.image.style.transform = `translate(${this.x}px, ${this.y}px) scale(${this.scale})`;
    }
  }

  class SvgViewerToolbar {
    constructor(element, viewer) {
      this.zoomLabel = element.querySelector('[data-action="reset"]');
      this.fullscreenButton = element.querySelector('[data-action="fullscreen"]');
      this.fullscreenButton.disabled = !document.fullscreenEnabled || !viewer.element.requestFullscreen;
      element.querySelector('[data-action="zoom-in"]').addEventListener("click", () => viewer.zoomBy(zoomStep));
      element.querySelector('[data-action="zoom-out"]').addEventListener("click", () => viewer.zoomBy(1 / zoomStep));
      this.zoomLabel.addEventListener("click", () => viewer.resetZoom());
      element.querySelector('[data-action="fit"]').addEventListener("click", () => viewer.fitToView());
      this.fullscreenButton.addEventListener("click", () => viewer.toggleFullscreen());
      element.hidden = false;
    }

    updateScale(scale) {
      this.zoomLabel.textContent = `${Math.round(scale * 1000) / 10}%`;
    }

    updateFullscreen(fullscreen) {
      this.fullscreenButton.setAttribute("aria-pressed", String(fullscreen));
      this.fullscreenButton.textContent = fullscreen ? "Exit full screen" : "Full screen";
    }
  }

  class SvgViewerInputHandler {
    constructor(element, viewer) {
      let drag;
      let dragged = false;
      element.addEventListener("click", () => {
        if (!dragged) viewer.activate();
      });
      element.addEventListener("wheel", (event) => {
        if (!viewer.active || !event.deltaY) return;

        event.preventDefault();
        const rect = element.getBoundingClientRect();
        viewer.zoomBy(event.deltaY < 0 ? zoomStep : 1 / zoomStep, {
          x: event.clientX - rect.left,
          y: event.clientY - rect.top
        });
      }, {passive: false});

      element.addEventListener("pointerdown", (event) => {
        if (drag || !event.isPrimary || event.button !== 0) return;

        dragged = false;
        drag = {id: event.pointerId, x: event.clientX, y: event.clientY};
        element.setPointerCapture(event.pointerId);
      });
      element.addEventListener("pointermove", (event) => {
        if (!drag || event.pointerId !== drag.id) return;

        const dx = event.clientX - drag.x;
        const dy = event.clientY - drag.y;
        if (!dx && !dy) return;

        dragged = true;
        element.classList.add("is-dragging");
        viewer.panBy(dx, dy);
        drag.x = event.clientX;
        drag.y = event.clientY;
      });
      const endDrag = (event) => {
        if (!drag || event.pointerId !== drag.id) return;

        drag = undefined;
        element.classList.remove("is-dragging");
      };
      element.addEventListener("pointerup", endDrag);
      element.addEventListener("pointercancel", endDrag);
      element.addEventListener("lostpointercapture", endDrag);
    }
  }

  class SvgDiagramViewer {
    constructor(element) {
      this.element = element;
      this.status = element.querySelector(".plantuml-viewer-status");
      this.active = false;
    }

    async initialize() {
      const viewport = this.element.querySelector(".plantuml-viewer-viewport");
      const image = viewport.querySelector("img");
      try {
        await image.decode();
      } catch (error) {
        this.showStatus("Could not load the PlantUML diagram.");
        console.error("Could not decode the PlantUML SVG.", error);
        return;
      }
      if (!image.naturalWidth || !image.naturalHeight) {
        this.showStatus("The PlantUML diagram has no display dimensions.");
        return;
      }

      this.viewport = new SvgViewerViewport(viewport, image);
      this.element.classList.add("is-ready");
      this.toolbar = new SvgViewerToolbar(this.element.querySelector(".plantuml-viewer-toolbar"), this);
      new SvgViewerInputHandler(viewport, this);
      // The browser handles Esc natively; fullscreenchange also covers that exit path.
      this.element.addEventListener("fullscreenchange", () => {
        this.toolbar.updateFullscreen(document.fullscreenElement === this.element);
        this.viewport.keepIntersectionVisible();
      });
    }

    activate() {
      this.active = true;
    }

    zoomBy(factor, anchor) {
      this.viewport.setScale(this.viewport.scale * factor, anchor);
      this.toolbar.updateScale(this.viewport.scale);
      this.showStatus("");
    }

    resetZoom() {
      this.viewport.setScale(1);
      this.toolbar.updateScale(this.viewport.scale);
      this.showStatus("");
    }

    panBy(dx, dy) {
      this.viewport.panBy(dx, dy);
    }

    fitToView() {
      if (!this.viewport.fitToView()) {
        this.showStatus("The diagram does not fit at the minimum zoom of 10%.");
        return;
      }
      this.toolbar.updateScale(this.viewport.scale);
      this.showStatus("");
    }

    async toggleFullscreen() {
      try {
        if (document.fullscreenElement === this.element) {
          await document.exitFullscreen();
        } else {
          await this.element.requestFullscreen();
        }
        this.showStatus("");
      } catch (error) {
        this.showStatus("Could not switch full-screen mode.");
        console.error("Could not switch PlantUML viewer full-screen mode.", error);
      }
    }

    showStatus(message) {
      this.status.textContent = message;
      this.status.hidden = !message;
    }
  }

  // GitBucket injects plugin JavaScript at the end of the page body.
  document.querySelectorAll(".plantuml-viewer").forEach((element) => new SvgDiagramViewer(element).initialize());
})();
