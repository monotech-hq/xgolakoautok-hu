
(ns app.packages.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:item-viewer/init-viewer! :packages.viewer
                                                            {:base-route      "/@app-home/packages"
                                                             :collection-name "packages"
                                                             :handler-key     :packages.viewer
                                                             :item-namespace  :package
                                                             :on-route        [:packages.viewer/load-viewer!]
                                                             :route-title     :packages}]
                                 [:router/add-route! :packages.viewer/products
                                                     {:client-event   [:packages.viewer/load-viewer! :products]
                                                      :restricted?    true
                                                      :route-parent   "/@app-home/packages"
                                                      :route-template "/@app-home/packages/:item-id/products"}]]}})
