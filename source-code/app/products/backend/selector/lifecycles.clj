
(ns app.products.backend.selector.lifecycles
    (:require [engines.item-lister.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :products.selector
                                              {:collection-name "products"
                                               :handler-key     :products.lister
                                               :item-namespace  :product}]})
