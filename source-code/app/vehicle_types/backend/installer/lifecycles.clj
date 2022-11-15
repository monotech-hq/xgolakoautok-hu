
(ns app.vehicle-types.backend.installer.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-launch {:fx [:vehicle-types.installer/check-install!]}})
