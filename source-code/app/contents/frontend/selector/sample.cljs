
(ns app.contents.frontend.selector.sample
    (:require [app.contents.frontend.api]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :load-my-selector!
  [:contents.selector/load-selector! :my-selector {:value-path [:my-item]}])
