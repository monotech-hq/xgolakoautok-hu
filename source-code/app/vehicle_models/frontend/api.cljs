
(ns app.vehicle-models.frontend.api
    (:require [app.vehicle-models.frontend.editor.effects]
              [app.vehicle-models.frontend.lifecycles]
              [app.vehicle-models.frontend.lister.effects]
              [app.vehicle-models.frontend.lister.lifecycles]
              [app.vehicle-models.frontend.selector.effects]
              [app.vehicle-models.frontend.viewer.effects]
              [app.vehicle-models.frontend.picker.views  :as picker.views]
              [app.vehicle-models.frontend.preview.views :as preview.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.vehicle-models.frontend.picker.views
(def model-picker picker.views/element)

; app.vehicle-models.frontend.preview.views
(def model-preview preview.views/element)
