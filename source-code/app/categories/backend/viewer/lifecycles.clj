
(ns app.categories.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:item-viewer/init-viewer! :categories.viewer
                                                            {:base-route      "/@app-home/categories"
                                                             :collection-name "categories"
                                                             :handler-key     :categories.viewer
                                                             :item-namespace  :category
                                                             :on-route        [:categories.viewer/load-viewer!]
                                                             :route-title     :categories}]
                                 [:router/add-route! :categories.viewer/models
                                                     {:client-event   [:categories.viewer/load-viewer! :models]
                                                      :restricted?    true
                                                      :route-parent   "/@app-home/categories"
                                                      :route-template "/@app-home/categories/:item-id/models"}]]}})
