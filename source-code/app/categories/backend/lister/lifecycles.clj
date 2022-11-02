
(ns app.categories.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :categories.lister
                                              {:base-route      "/@app-home/categories"
                                               :collection-name "categories"
                                               :handler-key     :categories.lister
                                               :item-namespace  :category
                                               :on-route        [:categories.lister/load-lister!]
                                               :route-title     :categories}]})
