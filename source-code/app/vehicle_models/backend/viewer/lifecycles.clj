
(ns app.vehicle-models.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:item-viewer/init-viewer! :vehicle-models.viewer
                                                            {:base-route      "/@app-home/vehicle-models"
                                                             :collection-name "vehicle_models"
                                                             :handler-key     :vehicle-models.viewer
                                                             :item-namespace  :model
                                                             :on-route        [:vehicle-models.viewer/load-viewer!]
                                                             :route-title     :vehicle-models}]
                                 [:x.router/add-route! :vehicle-models.viewer/types
                                                       {:client-event   [:vehicle-models.viewer/load-viewer! :types]
                                                        :js-build       :app
                                                        :restricted?    true
                                                        :parent-route   "/@app-home/vehicle-models"
                                                        :route-template "/@app-home/vehicle-models/:item-id/types"}]]}})
