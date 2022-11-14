
(ns site.xgo.pages.main-page.frontend.sections.categories.effects
  (:require [re-frame.api :as r]
            [mid-fruits.normalize :as normalize]))

; TEMP
(defn normalize-str [text]
  (-> text (str)
           (normalize/deaccent)
           (normalize/cut-special-chars "-+")
           (normalize/space->separator)
           (clojure.string/lower-case)))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(r/reg-fx
  ::set-url!
  (fn [url]
    (r/dispatch [:router/swap-to! url])))

(r/reg-event-fx
  :categories/select!
  (fn [_ [_ name]]
    {:dispatch-n [[:db/set-item! [:filters :category] (normalize-str name)]]
     ; ::set-url!  (str "/" (normalize-str name))}))
     :url/set-url! (str "/" (normalize-str name))}))
