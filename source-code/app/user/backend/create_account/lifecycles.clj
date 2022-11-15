
(ns app.user.backend.create-account.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.router/add-route! :user.create-account/route
                                         {:client-event   [:user.create-account/render!]
                                          :js-build       :app
                                          :route-template "/@app-home/create-account"}]})
