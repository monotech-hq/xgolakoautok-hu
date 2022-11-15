
(ns app.views.backend.terms-of-service.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.router/add-route! :views.terms-of-service/route
                                         {:client-event   [:views.terms-of-service/load-page!]
                                          :js-build       :app
                                          :restricted?    true
                                          :route-template "/@app-home/terms-of-service"}]})
