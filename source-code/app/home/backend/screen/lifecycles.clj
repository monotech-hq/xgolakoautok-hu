
(ns app.home.backend.screen.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.router/add-route! :home.screen/route
                                         {:client-event   [:home.screen/load-screen!]
                                          :js-build       :app
                                          :restricted?    true
                                          :route-template "/@app-home"}]})
