
(ns app.vehicle-types.frontend.api
    (:require [app.vehicle-types.frontend.editor.effects]
              [app.vehicle-types.frontend.lifecycles]
              [app.vehicle-types.frontend.preview.subs]
              [app.vehicle-types.frontend.selector.effects]
              [app.vehicle-types.frontend.viewer.effects]
              [app.vehicle-types.frontend.lister.views  :as lister.views]
              [app.vehicle-types.frontend.picker.views  :as picker.views]
              [app.vehicle-types.frontend.preview.views :as preview.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.vehicle-types.frontend.lister.views
(def type-lister lister.views/type-lister)

; app.vehicle-types.frontend.picker.views
(def type-picker picker.views/element)

; app.vehicle-types.frontend.preview.views
(def type-preview preview.views/element)
