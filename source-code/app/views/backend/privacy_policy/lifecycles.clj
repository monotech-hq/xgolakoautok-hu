
(ns app.views.backend.privacy-policy.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.router/add-route! :views.privacy-policy/route
                                         {:client-event   [:views.privacy-policy/load-page!]
                                          :js-build       :app
                                          :restricted?    true
                                          :route-template "/@app-home/privacy-policy"}]})
