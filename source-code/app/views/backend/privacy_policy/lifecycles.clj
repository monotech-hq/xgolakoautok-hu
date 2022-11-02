
(ns app.views.backend.privacy-policy.lifecycles
    (:require [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:router/add-route! :views.privacy-policy/route
                                       {:client-event   [:views.privacy-policy/load-page!]
                                        :restricted?    true
                                        :route-template "/@app-home/privacy-policy"}]})
