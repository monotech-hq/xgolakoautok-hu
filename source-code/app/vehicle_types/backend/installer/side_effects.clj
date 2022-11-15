
(ns app.vehicle-types.backend.installer.side-effects
    (:require [app.schemes.backend.api                    :as schemes]
              [app.vehicle-types.backend.installer.config :as installer.config]
              [re-frame.api                               :as r]
              [x.user.api                              :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- check-install!
  [_]
  (if-not (schemes/form-exists? :vehicle-types.technical-data)
          (schemes/save-form! {:session x.user/SYSTEM-USER-ACCOUNT}
                              :vehicle-types.technical-data
                              {:scheme/fields installer.config/INITIAL-FIELDS})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-fx :vehicle-types.installer/check-install! check-install!)
