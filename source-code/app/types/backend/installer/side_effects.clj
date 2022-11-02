
(ns app.types.backend.installer.side-effects
    (:require [app.schemes.backend.api            :as schemes]
              [app.types.backend.installer.config :as installer.config]
              [re-frame.api                       :as r]
              [x.server-user.api                  :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- check-install!
  [_]
  (if-not (schemes/form-exists? :types.technical-data)
          (schemes/save-form! {:session x.user/SYSTEM-USER-ACCOUNT}
                              :types.technical-data
                              {:scheme/fields installer.config/INITIAL-FIELDS})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-fx :types.installer/check-install! check-install!)
