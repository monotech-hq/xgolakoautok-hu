
(ns app.vehicle-categories.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :vehicle-categories.lister
                                              {:base-route      "/@app-home/vehicle-categories"
                                               :collection-name "vehicle_categories"
                                               :handler-key     :vehicle-categories.lister
                                               :item-namespace  :category
                                               :on-route        [:vehicle-categories.lister/load-lister!]
                                               :route-title     :vehicle-categories}]})
