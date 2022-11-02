
(ns app.price-quote-templates.backend.selector.lifecycles
    (:require [engines.item-lister.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :price-quote-templates.selector
                                              {:collection-name "price_quote_templates"
                                               :handler-key     :price-quote-templates.lister
                                               :item-namespace  :template}]})
