
(ns app.views.frontend.blank-page.events
    (:require [map.api      :refer [dissoc-in]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-page!
  [db _]
  (dissoc-in db [:views :blank-page/page-loaded?]))

(defn page-loaded
  [db _]
  (assoc-in db [:views :blank-page/page-loaded?] true))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-db :views.blank-page/page-loaded page-loaded)
