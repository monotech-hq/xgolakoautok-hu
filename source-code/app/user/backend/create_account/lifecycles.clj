
(ns app.user.backend.create-account.lifecycles
    (:require [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:router/add-route! :user.create-account/route
                                       {:client-event   [:user.create-account/render!]
                                        :route-template "/@app-home/create-account"}]})
