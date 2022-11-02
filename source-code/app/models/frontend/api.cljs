
(ns app.models.frontend.api
    (:require [app.models.frontend.editor.effects]
              [app.models.frontend.lifecycles]
              [app.models.frontend.lister.effects]
              [app.models.frontend.lister.lifecycles]
              [app.models.frontend.selector.effects]
              [app.models.frontend.viewer.effects]
              [app.models.frontend.picker.views  :as picker.views]
              [app.models.frontend.preview.views :as preview.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.models.frontend.picker.views
(def model-picker picker.views/element)

; app.models.frontend.preview.views
(def model-preview preview.views/element)
