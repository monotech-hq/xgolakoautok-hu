
(ns app.products.backend.preview.lifecycles
    (:require [engines.item-preview.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-preview/init-preview! :products.preview
                                                {:collection-name "products"
                                                 :handler-key     :products.preview
                                                 :item-namespace  :product}]})
