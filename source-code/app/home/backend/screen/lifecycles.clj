
(ns app.home.backend.screen.lifecycles
    (:require [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:router/add-route! :home.screen/route
                                       {:client-event   [:home.screen/load-screen!]
                                        :restricted?    true
                                        :route-template "/@app-home"}]})
