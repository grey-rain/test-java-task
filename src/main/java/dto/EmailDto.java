package dto;

import org.apache.commons.lang3.StringUtils;

public class EmailDto {
    private String relatedAddress;
    private String subj;
    private String msgBody;

    public String getRelatedAddress() {
        return relatedAddress;
    }

    public void setRelatedAddress(String relatedAddress) {
        this.relatedAddress = relatedAddress;
    }

    public String getSubj() {
        return subj;
    }

    public void setSubj(String subj) {
        this.subj = subj;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EmailDto) {
            EmailDto comparisonCandidate = (EmailDto) obj;
            return StringUtils.equals(this.getRelatedAddress(), comparisonCandidate.getRelatedAddress()) && StringUtils
                    .equals(this.getSubj(), comparisonCandidate.getSubj()) && StringUtils
                    .equals(this.getMsgBody(), comparisonCandidate.getMsgBody());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return String.format("%s%s%s", this.getRelatedAddress(), this.getSubj(), this.getMsgBody()).hashCode();
    }
}
