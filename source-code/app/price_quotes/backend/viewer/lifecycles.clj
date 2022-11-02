
(ns app.price-quotes.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-viewer/init-viewer! :price-quotes.viewer
                                              {:base-route      "/@app-home/price-quotes"
                                               :collection-name "price_quotes"
                                               :handler-key     :price-quotes.viewer
                                               :item-namespace  :price-quote
                                               :on-route        [:price-quotes.viewer/load-viewer!]
                                               :route-title     :price-quotes}]})
