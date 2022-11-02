
(ns app.services.frontend.api
    (:require [app.services.frontend.editor.effects]
              [app.services.frontend.lifecycles]
              [app.services.frontend.lister.effects]
              [app.services.frontend.lister.lifecycles]
              [app.services.frontend.selector.effects]
              [app.services.frontend.viewer.effects]
              [app.services.frontend.picker.views  :as picker.views]
              [app.services.frontend.preview.views :as preview.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.services.frontend.picker.views
(def service-picker picker.views/element)

; app.services.frontend.preview.views
(def service-preview preview.views/element)
