
(ns app.user.backend.forgot-password.lifecycles
    (:require [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:router/add-route! :user.forgot-password/route
                                       {:client-event   [:user.forgot-password/render!]
                                        :route-template "/@app-home/forgot-password"}]})
