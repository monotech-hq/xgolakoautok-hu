
(ns app.price-quote-inquiries.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :price-quote-inquiries.lister
                                              {:base-route      "/@app-home/price-quote-inquiries"
                                               :collection-name "price_quote_inquiries"
                                               :handler-key     :price-quote-inquiries.lister
                                               :item-namespace  :template
                                               :on-route        [:price-quote-inquiries.lister/load-lister!]
                                               :route-title     :price-quote-inquiries}]})
