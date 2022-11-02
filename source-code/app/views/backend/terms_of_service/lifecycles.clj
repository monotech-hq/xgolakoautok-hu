
(ns app.views.backend.terms-of-service.lifecycles
    (:require [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:router/add-route! :views.terms-of-service/route
                                       {:client-event   [:views.terms-of-service/load-page!]
                                        :restricted?    true
                                        :route-template "/@app-home/terms-of-service"}]})
