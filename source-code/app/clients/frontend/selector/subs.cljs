
(ns app.clients.frontend.selector.subs
    (:require [string.api :as string]
              [re-frame.api      :as r :refer [r]]
              [x.locales.api     :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-name
  [db [_ item-dex]]
  (let [first-name  (get-in db [:clients :selector/downloaded-items item-dex :first-name])
        last-name   (get-in db [:clients :selector/downloaded-items item-dex :last-name])
        client-name (r x.locales/get-ordered-name db first-name last-name)]
      ; XXX#6071
      (string/trim client-name)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :clients.selector/get-client-name get-client-name)
