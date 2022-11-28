
(ns app.settings.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:item-editor/init-editor! :settings.editor
                                                            {:collection-name "user_settings"
                                                             :handler-key     :settings.editor
                                                             :item-namespace  :user-settings}]
                                 [:x.router/add-route! :settings.editor/base-route
                                                       {:client-event   [:settings.editor/load-editor!]
                                                        :js-build       :app
                                                        :restricted?    true
                                                        :route-template "/@app-home/settings"}]
                                 [:x.router/add-route! :settings.editor/extended-route
                                                       {:client-event   [:settings.editor/load-editor!]
                                                        :js-build       :app
                                                        :restricted?    true
                                                        :parent-route   "/@app-home"
                                                        :route-template "/@app-home/settings/:view-id"}]]}})
