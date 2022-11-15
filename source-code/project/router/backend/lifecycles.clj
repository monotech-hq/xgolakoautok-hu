
(ns project.router.backend.lifecycles
    (:require [project.router.backend.default-routes :as default-routes]
              [project.router.backend.main-routes    :as main-routes]
              [x.core.api                     :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init {:dispatch-n [[:x.router/set-default-route! :method-not-allowed default-routes/METHOD-NOT-ALLOWED]
                                 [:x.router/set-default-route! :not-acceptable     default-routes/NOT-ACCEPTABLE]
                                 [:x.router/set-default-route! :not-found          default-routes/NOT-FOUND]
                                 [:x.router/add-routes!                            main-routes/ROUTES]]}})
