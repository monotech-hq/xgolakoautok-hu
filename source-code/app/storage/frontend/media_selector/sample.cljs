
(ns app.storage.frontend.media-selector.sample
    (:require [app.storage.frontend.api]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :load-my-media-selector!
  [:storage.media-selector/load-selector! :my-selector {:value-path [:my-item]}])
