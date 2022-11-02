
(ns project.router.backend.lifecycles
    (:require [project.router.backend.default-routes :as default-routes]
              [project.router.backend.main-routes    :as main-routes]
              [x.server-core.api                     :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init {:dispatch-n [[:router/set-default-route! :method-not-allowed default-routes/METHOD-NOT-ALLOWED]
                                 [:router/set-default-route! :not-acceptable     default-routes/NOT-ACCEPTABLE]
                                 [:router/set-default-route! :not-found          default-routes/NOT-FOUND]
                                 [:router/add-routes!                            main-routes/ROUTES]]}})
