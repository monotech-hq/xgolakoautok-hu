
(ns app.price-quote-templates.backend.preview.lifecycles
    (:require [engines.item-preview.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-preview/init-preview! :price-quote-templates.preview
                                                {:collection-name "price_quote_templates"
                                                 :handler-key     :price-quote-templates.preview
                                                 :item-namespace  :template}]})
