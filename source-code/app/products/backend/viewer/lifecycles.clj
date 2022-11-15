
(ns app.products.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-viewer/init-viewer! :products.viewer
                                              {:base-route      "/@app-home/products"
                                               :collection-name "products"
                                               :handler-key     :products.viewer
                                               :item-namespace  :product
                                               :on-route        [:products.viewer/load-viewer!]
                                               :route-title     :products}]})
