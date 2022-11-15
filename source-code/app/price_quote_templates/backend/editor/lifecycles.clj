
(ns app.price-quote-templates.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :price-quote-templates.editor
                                              {:base-route      "/@app-home/price-quote-templates"
                                               :collection-name "price_quote_templates"
                                               :handler-key     :price-quote-templates.editor
                                               :item-namespace  :template
                                               :on-route        [:price-quote-templates.editor/load-editor!]
                                               :route-title     :price-quote-templates}]})
