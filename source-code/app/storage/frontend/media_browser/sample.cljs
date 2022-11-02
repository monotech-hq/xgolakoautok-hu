
(ns app.storage.frontend.media-browser.sample
    (:require [app.storage.frontend.api]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :load-my-media-browser!
  [:router/go-to! "/@app-home/storage"])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :load-your-media-browser!
  [:router/go-to! "/@app-home/storage/my-directory"])
