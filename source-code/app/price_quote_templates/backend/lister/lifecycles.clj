
(ns app.price-quote-templates.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :price-quote-templates.lister
                                              {:base-route      "/@app-home/price-quote-templates"
                                               :collection-name "price_quote_templates"
                                               :handler-key     :price-quote-templates.lister
                                               :item-namespace  :template
                                               :on-route        [:price-quote-templates.lister/load-lister!]
                                               :route-title     :price-quote-templates}]})
