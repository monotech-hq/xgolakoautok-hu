
(ns app.products.frontend.api
    (:require [app.products.frontend.editor.effects]
              [app.products.frontend.lifecycles]
              [app.products.frontend.lister.effects]
              [app.products.frontend.lister.lifecycles]
              [app.products.frontend.selector.effects]
              [app.products.frontend.viewer.effects]
              [app.products.frontend.picker.views  :as picker.views]
              [app.products.frontend.preview.views :as preview.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.products.frontend.picker.views
(def product-picker picker.views/element)

; app.products.frontend.preview.views
(def product-preview preview.views/element)
