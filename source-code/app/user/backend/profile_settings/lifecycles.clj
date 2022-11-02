
(ns app.user.backend.profile-settings.lifecycles
    (:require [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:router/add-route! :user.profile-settings/base-route
                                       {:client-event   [:user.profile-settings/load-page!]
                                        :restricted?    true
                                        :route-template "/@app-home/user-profile"}]})
