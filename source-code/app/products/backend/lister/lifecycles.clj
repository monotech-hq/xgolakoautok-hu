
(ns app.products.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :products.lister
                                              {:base-route      "/@app-home/products"
                                               :collection-name "products"
                                               :handler-key     :products.lister
                                               :item-namespace  :product
                                               :on-route        [:products.lister/load-lister!]
                                               :route-title     :products}]})
