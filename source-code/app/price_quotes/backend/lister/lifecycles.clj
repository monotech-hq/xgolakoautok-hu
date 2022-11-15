
(ns app.price-quotes.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :price-quotes.lister
                                              {:base-route      "/@app-home/price-quotes"
                                               :collection-name "price_quotes"
                                               :handler-key     :price-quotes.lister
                                               :item-namespace  :price-quote
                                               :on-route        [:price-quotes.lister/load-lister!]
                                               :route-title     :price-quotes}]})
