
(ns app.price-quote-templates.frontend.api
    (:require [app.price-quote-templates.frontend.editor.effects]
              [app.price-quote-templates.frontend.lister.effects]
              [app.price-quote-templates.frontend.lister.lifecycles]
              [app.price-quote-templates.frontend.viewer.effects]
              [app.price-quote-templates.frontend.lifecycles]
              [app.price-quote-templates.frontend.selector.effects]
              [app.price-quote-templates.frontend.picker.views  :as picker.views]
              [app.price-quote-templates.frontend.preview.views :as preview.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.price-quote-templates.frontend.picker.views
(def template-picker picker.views/element)

; app.price-quote-templates.frontend.preview.views
(def template-preview preview.views/element)
