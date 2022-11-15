
(ns app.models.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:item-viewer/init-viewer! :models.viewer
                                                            {:base-route      "/@app-home/models"
                                                             :collection-name "models"
                                                             :handler-key     :models.viewer
                                                             :item-namespace  :model
                                                             :on-route        [:models.viewer/load-viewer!]
                                                             :route-title     :models}]
                                 [:x.router/add-route! :models.viewer/types
                                                     {:client-event   [:models.viewer/load-viewer! :types]
                                                      :restricted?    true
                                                      :route-parent   "/@app-home/models"
                                                      :route-template "/@app-home/models/:item-id/types"}]]}})
