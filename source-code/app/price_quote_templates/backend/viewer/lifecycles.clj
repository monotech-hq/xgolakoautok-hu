
(ns app.price-quote-templates.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-viewer/init-viewer! :price-quote-templates.viewer
                                              {:base-route      "/@app-home/price-quote-templates"
                                               :collection-name "price_quote_templates"
                                               :handler-key     :price-quote-templates.viewer
                                               :item-namespace  :template
                                               :on-route        [:price-quote-templates.viewer/load-viewer!]
                                               :route-title     :price-quote-templates}]})
