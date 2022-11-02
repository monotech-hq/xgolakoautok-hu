
(ns app.price-quotes.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.server-core.api :as core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :price-quotes.editor
                                              {:base-route      "/@app-home/price-quotes"
                                               :collection-name "price_quotes"
                                               :handler-key     :price-quotes.editor
                                               :item-namespace  :price-quote
                                               :on-route        [:price-quotes.editor/load-editor!]
                                               :route-title     :price-quotes}]})
