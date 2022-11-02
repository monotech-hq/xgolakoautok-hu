
(ns app.products.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :products.editor
                                              {:base-route      "/@app-home/products"
                                               :collection-name "products"
                                               :handler-key     :products.editor
                                               :item-namespace  :product
                                               :on-route        [:products.editor/load-editor!]
                                               :route-title     :products}]})
