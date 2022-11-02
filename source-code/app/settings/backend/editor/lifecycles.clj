
(ns app.settings.backend.editor.lifecycles
    (:require [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:item-editor/init-editor! :settings.editor
                                                            {:collection-name "user_settings"
                                                             :handler-key     :settings.editor
                                                             :item-namespace  :user-settings}]
                                 [:router/add-route! :settings.editor/base-route
                                                     {:client-event   [:settings.editor/load-editor!]
                                                      :restricted?    true
                                                      :route-template "/@app-home/settings"}]
                                 [:router/add-route! :settings.editor/extended-route
                                                     {:client-event   [:settings.editor/load-editor!]
                                                      :restricted?    true
                                                      :route-parent   "/@app-home"
                                                      :route-template "/@app-home/settings/:view-id"}]]}})
