
(ns app.vehicle-categories.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:item-viewer/init-viewer! :vehicle-categories.viewer
                                                            {:base-route      "/@app-home/vehicle-categories"
                                                             :collection-name "vehicle_categories"
                                                             :handler-key     :vehicle-categories.viewer
                                                             :item-namespace  :category
                                                             :on-route        [:vehicle-categories.viewer/load-viewer!]
                                                             :route-title     :vehicle-categories}]
                                 [:x.router/add-route! :vehicle-categories.viewer/models
                                                       {:client-event   [:vehicle-categories.viewer/load-viewer! :models]
                                                        :js-build       :app
                                                        :restricted?    true
                                                        :route-parent   "/@app-home/vehicle-categories"
                                                        :route-template "/@app-home/vehicle-categories/:item-id/models"}]]}})
