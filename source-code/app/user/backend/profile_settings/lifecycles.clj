
(ns app.user.backend.profile-settings.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.router/add-route! :user.profile-settings/base-route
                                         {:client-event   [:user.profile-settings/load-page!]
                                          :js-build       :app
                                          :restricted?    true
                                          :route-template "/@app-home/user-profile"}]})
