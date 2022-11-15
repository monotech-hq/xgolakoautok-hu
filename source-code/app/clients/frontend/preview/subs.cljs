
(ns app.clients.frontend.preview.subs
    (:require [mid-fruits.string :as string]
              [re-frame.api      :as r :refer [r]]
              [x.locales.api     :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-name
  [db [_ picker-id]]
  (let [first-name  (get-in db [:clients :preview/downloaded-items picker-id :first-name])
        last-name   (get-in db [:clients :preview/downloaded-items picker-id :last-name])
        client-name (r x.locales/get-ordered-name db first-name last-name)]
      ; XXX#6071
      (string/trim client-name)))

(defn get-client-address
  [db [_ picker-id]]
  (let [zip-code (get-in db [:clients :preview/downloaded-items picker-id :zip-code])
        country  (get-in db [:clients :preview/downloaded-items picker-id :country])
        city     (get-in db [:clients :preview/downloaded-items picker-id :city])
        address  (get-in db [:clients :preview/downloaded-items picker-id :address])]
       (r x.locales/get-ordered-address db zip-code country city address)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :clients.preview/get-client-address get-client-address)
(r/reg-sub :clients.preview/get-client-name    get-client-name)
