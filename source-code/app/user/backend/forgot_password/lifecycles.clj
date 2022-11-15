
(ns app.user.backend.forgot-password.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.router/add-route! :user.forgot-password/route
                                         {:client-event   [:user.forgot-password/render!]
                                          :js-build       :app
                                          :route-template "/@app-home/forgot-password"}]})
