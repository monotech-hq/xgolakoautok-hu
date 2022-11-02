
(ns app.clients.frontend.viewer.subs
    (:require [mid-fruits.string :as string]
              [re-frame.api      :as r :refer [r]]
              [x.app-locales.api :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-name
  [db _]
  (let [first-name  (get-in db [:clients :viewer/viewed-item :first-name])
        last-name   (get-in db [:clients :viewer/viewed-item :last-name])
        client-name (r x.locales/get-ordered-name db first-name last-name)]
       ; XXX#6071
       ; A viewer label és breadcrumbs komponensében az :unnamed-client
       ; felirat jelenik meg, ha a kliens neve nincs kitöltve.
       ; Ennek megállapításához szükséges a string/trim függvény alkalmazása,
       ; hogy a first-name és last-name közötti szünet karaktert ne érzékelje
       ; tartalomnak abban az esetben, amikor a first-name és a last-name értéke
       ; is hiányzik!
       (string/trim client-name)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :clients.viewer/get-client-name get-client-name)
